<?php

namespace App\Http\Resources;

use iLaravel\Core\iApp\Http\Resources\Resource;

class SiadatRecord extends Resource
{
    public function toArray($request)
    {
        $data = parent::toArray($request);
        return $data;
    }
}
