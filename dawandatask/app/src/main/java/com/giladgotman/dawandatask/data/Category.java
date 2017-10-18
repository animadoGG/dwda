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

package com.giladgotman.dawandatask.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Immutable model class for a Task.
 */
public final class Category {

    @NonNull
    public final String id;

    @Nullable
    public final String name;

    @Nullable
    public final String image_url;


    public Category(@NonNull String id) {
        this.id = id;
        this.name = null;
        this.image_url = null;
    }

    public Category(@NonNull String id, String name, String image_url) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (!id.equals(category.id)) return false;
        if (name != null ? !name.equals(category.name) : category.name != null) return false;
        return image_url != null ? image_url.equals(category.image_url) : category.image_url == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (image_url != null ? image_url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
