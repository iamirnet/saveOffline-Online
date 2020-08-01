<?php

function siadat_path($path = null)
{
    $path = trim($path, '/');
    return __DIR__ . ($path ? "/$path" : '');
}

function isiadat($key = null, $default = null)
{
    return config('iamir'.($key ? ".$key" : ''), $default);
}
