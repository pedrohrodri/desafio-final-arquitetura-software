package com.pedrorodrigues.desafiofinal.core.util

import java.util.Optional

fun <T> Optional<T>.takeExistsAndIf(predicate: (T) -> Boolean): T? =
    this.orElse(null)?.takeIf(predicate)