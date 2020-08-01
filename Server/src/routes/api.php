<?php

Route::namespace('v1')->prefix('v1/siadat/')->group(function () {
    Route::apiResource('records', 'SiadatRecordController', ['as' => 'api.siadat']);
});
