package com.giladgotman.dawandatask.categories;

import com.giladgotman.dawandatask.di.ActivityScoped;
import com.giladgotman.dawandatask.di.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * {@link CategoriesPresenter}.
 */
@Module
public abstract class CategoriesModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract CategoriesFragment tasksFragment();

    @ActivityScoped
    @Binds abstract CategoriesContract.Presenter taskPresenter(CategoriesPresenter presenter);
}
