<?php


namespace App\Http\Controllers\API\v1\SiadatRecord;


trait Filters
{
    public function filters($request, $model, $parent = null, $operators = [])
    {
        $user = auth()->user();
        $filters = [];
        $current = [];
        $filters = [
            [
                'name' => 'all',
                'title' => _t('all'),
                'type' => 'text',
            ],
            [
                'name' => 'name',
                'title' => _t('name'),
                'type' => 'text'
            ],
            [
                'name' => 'family',
                'title' => _t('family'),
                'type' => 'text'
            ],
            [
                'name' => 'level',
                'title' => _t('level'),
                'type' => 'text'
            ],
        ];
        $this->requestFilter($request, $model, $parent, $filters, $operators);
        if ($request->q) {
            $this->searchQ($request, $model, $parent);
            $current['q'] = $request->q;
        }
        return [$filters, $current, $operators];
    }
}
