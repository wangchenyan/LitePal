/*
 * Copyright (C)  Tony Green, LitePal Framework Open Source Project
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
package org.litepal.copy.crud.async;

import org.litepal.copy.crud.callback.CountCallback;

/**
 * Executor for count query in background.
 *
 * @author Tony Green
 * @since 2017/2/22
 */
public class CountExecutor extends AsyncExecutor {

    private CountCallback cb;

    /**
     * Register a callback listener and async task will start executing right away.
     * @param callback
     *          Callback for count query in background.
     */
    public void listen(CountCallback callback) {
        cb = callback;
        execute();
    }

    public CountCallback getListener() {
        return  cb;
    }

}