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

import android.support.annotation.Nullable;
import com.giladgotman.dawandatask.BasePresenter;
import com.giladgotman.dawandatask.BaseView;
import com.giladgotman.dawandatask.data.Category;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CategoriesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showCategories(List<Category> categories);

        boolean isActive();

        void showError(@Nullable Throwable e, @Nullable String message);
    }

    interface Presenter extends BasePresenter<View> {

        void loadCategories();

        void takeView(CategoriesContract.View view);

        void dropView();
    }
}
