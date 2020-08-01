<?php

namespace App\Http\Controllers\API\v1\SiadatRecord;

use iLaravel\Core\iApp\Http\Requests\iLaravel as Request;

trait Rules
{
    public function rules(Request $request, $action, $parent = null, $unique = null)
    {
        $rules = [];
        switch ($action) {
            case 'store':
            case 'update':
                $rules = [
                    'name' => "required|string",
                    'family' => "required|string",
                    'level' => "nullable|string|numeric",
                ];
                break;
        }
        $unique = $request->has('unique') ? $request->unique : $unique;
        if ($unique) return str_replace(['required'], ['nullable'], $rules[$unique]);
        return $rules;
    }
}
