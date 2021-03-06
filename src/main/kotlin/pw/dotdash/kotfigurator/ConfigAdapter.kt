/*
 * MIT License
 *
 * Copyright (c) 2018 Christian Hughes
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pw.dotdash.kotfigurator

import com.google.common.reflect.TypeToken
import kotlin.reflect.KProperty

interface ConfigAdapter {

    fun <T> value(key: String? = null, comment: String? = null, type: TypeToken<T>, default: () -> T): Value<T>

    fun string(key: String? = null, comment: String? = null, default: () -> String = { "" }): Value<String>

    fun boolean(key: String? = null, comment: String? = null, default: () -> Boolean = { false }): Value<Boolean>

    fun int(key: String? = null, comment: String? = null, default: () -> Int = { 0 }): Value<Int>

    fun long(key: String? = null, comment: String? = null, default: () -> Long = { 0 }): Value<Long>

    fun float(key: String? = null, comment: String? = null, default: () -> Float = { 0.0f }): Value<Float>

    fun double(key: String? = null, comment: String? = null, default: () -> Double = { 0.0 }): Value<Double>

    fun <E> list(key: String? = null, comment: String? = null, elementType: TypeToken<E>, default: () -> List<E> = { listOf() }): Value<List<E>>

    fun <K, V> map(key: String? = null, comment: String? = null, keyType: TypeToken<K>, valueType: TypeToken<V>, default: () -> Map<K, V> = { mapOf() }): Value<Map<K, V>>

    fun <T> section(key: String? = null, comment: String? = null, init: (ConfigAdapter) -> T): Section<T>

    interface Value<T> {

        val key: String?

        val comment: String?

        operator fun getValue(self: Any?, property: KProperty<*>): T

        operator fun setValue(self: Any?, property: KProperty<*>, value: T)
    }

    interface Section<T> {

        val key: String?

        val comment: String?

        operator fun getValue(self: Any?, property: KProperty<*>): T
    }
}

inline fun <reified T> ConfigAdapter.value(key: String? = null,
                                           comment: String? = null,
                                           noinline default: () -> T): ConfigAdapter.Value<T> =
        this.value(key, comment, object : TypeToken<T>() {}, default)

inline fun <reified E> ConfigAdapter.list(key: String? = null,
                                          comment: String? = null,
                                          noinline default: () -> List<E> = { listOf() }): ConfigAdapter.Value<List<E>> =
        this.list(key, comment, object : TypeToken<E>() {}, default)

inline fun <reified K, reified V> ConfigAdapter.map(key: String? = null,
                                                    comment: String? = null,
                                                    noinline default: () -> Map<K, V> = { mapOf() }): ConfigAdapter.Value<Map<K, V>> =
        this.map(key, comment, object : TypeToken<K>() {}, object : TypeToken<V>() {}, default)