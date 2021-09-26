/*
 * Copyright (c) 2020-2021, wvkity(wvkity@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.mybatisx.backup.convert;

import io.github.mybatisx.Objects;
import io.github.mybatisx.reflect.Reflections;
import io.github.mybatisx.reflection.MetaObjects;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.BeanUtils;

/**
 * 默认Bean转换器
 * @author wvkity
 * @created 2021-07-19
 * @since 1.0.0
 */
public class DefaultBeanConverter implements BeanConverter {

    @Override
    public <T> T convert(Object source, String sourceIdProp, Class<T> target, String targetIdProp) throws Exception {
        final T targetBean = Reflections.newInstance(target);
        final boolean hasIdProp = Objects.isNotBlank(sourceIdProp);
        if (hasIdProp) {
            BeanUtils.copyProperties(source, targetBean, sourceIdProp);
        } else {
            BeanUtils.copyProperties(source, targetBean);
        }
        if (hasIdProp && Objects.isNotBlank(targetIdProp)) {
            final MetaObject sourceMetaObject = MetaObjects.forObject(source);
            final MetaObject targetMetaObject = MetaObjects.forObject(targetBean);
            targetMetaObject.setValue(targetIdProp, sourceMetaObject.getValue(sourceIdProp));
        }
        return targetBean;
    }
}
