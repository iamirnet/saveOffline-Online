<?php

namespace App\Http\Controllers\API\v1;

use iLaravel\Core\iApp\Http\Controllers\API\Controller;
use iLaravel\Core\iApp\Http\Controllers\API\Methods\Controller\Destroy;
use iLaravel\Core\iApp\Http\Controllers\API\Methods\Controller\Index;
use iLaravel\Core\iApp\Http\Controllers\API\Methods\Controller\Show;
use iLaravel\Core\iApp\Http\Controllers\API\Methods\Controller\Store;
use iLaravel\Core\iApp\Http\Controllers\API\Methods\Controller\Update;
use iLaravel\Core\iApp\Http\Requests\iLaravel as Request;


class SiadatRecordController extends Controller
{
    use Index,
        Show,
        Store,
        SiadatRecord\Rules,
        SiadatRecord\Filters,
        SiadatRecord\SearchQ;

    public function update(Request $request, \App\SiadatRecord $record)
    {
        if ($record->id == 1) return $this->_show($request, $record);
        return $this->_update($request, $record);
    }

    public function destroy(Request $request, \App\SiadatRecord $record)
    {
        if ($record->id == 1) return $this->_show($request, $record);
        return $this->_destroy($request, $record);
    }
}
