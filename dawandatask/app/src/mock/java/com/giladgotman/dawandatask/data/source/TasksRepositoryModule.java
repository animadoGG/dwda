package com.giladgotman.dawandatask.data.source;

import com.giladgotman.dawandatask.data.FakeTasksRemoteDataSource;
import com.giladgotman.dawandatask.data.source.local.TasksLocalDataSource;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * This is used by Dagger to inject the required arguments into the {@link TasksRepository}.
 */
@Module
abstract public class TasksRepositoryModule {

    @Singleton
    @Binds
    @Local
    abstract TasksDataSource provideTasksLocalDataSource(TasksLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract TasksDataSource provideTasksRemoteDataSource(FakeTasksRemoteDataSource dataSource);
}
