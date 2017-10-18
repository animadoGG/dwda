/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.giladgotman.dawandatask.categories;

import com.giladgotman.dawandatask.data.Category;
import com.giladgotman.dawandatask.data.Task;
import com.giladgotman.dawandatask.data.source.TasksDataSource;
import com.giladgotman.dawandatask.data.source.TasksRepository;
import com.giladgotman.dawandatask.di.ActivityScoped;
import com.giladgotman.dawandatask.tasks.TasksFilterType;
import com.giladgotman.dawandatask.tasks.TasksFragment;
import com.giladgotman.dawandatask.tasks.TasksModule;
import com.giladgotman.dawandatask.util.EspressoIdlingResource;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


/**
 * Listens to user actions from the UI ({@link TasksFragment}), retrieves the data and updates the
 * UI as required.
 * <p/>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link TasksModule} to do so.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
@ActivityScoped
final class CategoriesPresenter implements CategoriesContract.Presenter {

    private final TasksRepository mTasksRepository;
    @Nullable
    private CategoriesContract.View categoriesView;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    CategoriesPresenter(TasksRepository tasksRepository) {
        mTasksRepository = tasksRepository;
    }


    @Override
    public void loadCategories() {
        // Simplification for sample: a network reload will be forced on first load.
        loadCategories(mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link TasksDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadCategories(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            if (categoriesView != null) {
                categoriesView.setLoadingIndicator(true);
            }
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (categoriesView == null || !categoriesView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    categoriesView.setLoadingIndicator(false);
                }

                processTasks(null);// TODO: 19-10-2017 use categories
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!categoriesView.isActive()) {
                    return;
                }
                categoriesView.showError(null, "Error loading data");
            }
        });
    }

    private void processTasks(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            if (categoriesView != null) {
                categoriesView.showCategories(categories);
            }
        }

    }


    private void processEmptyTasks() {
        if (categoriesView == null) return;
        categoriesView.showError(null, "No categories");
    }


    @Override
    public void takeView(CategoriesContract.View view) {
        this.categoriesView = view;
        loadCategories();
    }

    @Override
    public void dropView() {
        categoriesView = null;
    }
}
