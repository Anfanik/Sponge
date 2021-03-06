/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.registry;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.registry.DefaultedRegistryType;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class SpongeDefaultedRegistryType<T> extends SpongeRegistryType<T> implements DefaultedRegistryType<T> {

    private final Supplier<RegistryHolder> defaultHolder;

    public SpongeDefaultedRegistryType(final ResourceKey root, final ResourceKey location, final Supplier<RegistryHolder> defaultHolder) {
        super(root, location);
        this.defaultHolder = Objects.requireNonNull(defaultHolder, "defaultHolder");
    }

    @Override
    public Registry<T> get() {
        return this.defaultHolder.get().registry(this);
    }

    @Override
    public Optional<Registry<T>> find() {
        return this.defaultHolder.get().findRegistry(this);
    }

    @Override
    public Supplier<RegistryHolder> defaultHolder() {
        return this.defaultHolder;
    }
}
