/*
 * Copyright (C)  Tony Green, Litepal Framework Open Source Project
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

package org.litepal.copy.tablemanager.typechange;

/**
 * This class deals with boolean type.
 * 
 * @author Tony Green
 * @since 1.0
 */
public class BooleanOrm extends OrmChange {

	/**
	 * If the field type passed in is boolean, it will change it into integer as
	 * column type.
	 */
	@Override
	public String object2Relation(String fieldType) {
		if (fieldType != null) {
			if (fieldType.equals("boolean") || fieldType.equals("java.lang.Boolean")) {
				return "integer";
			}
		}
		return null;
	}

}
