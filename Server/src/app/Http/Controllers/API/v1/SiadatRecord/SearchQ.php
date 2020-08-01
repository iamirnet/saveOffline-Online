<?php


namespace App\Http\Controllers\API\v1\SiadatRecord;


trait SearchQ
{
    public function searchQ($request, $model, $parent)
    {
        $q = $request->q;
        $model->where(function ($query) use ($q) {
            $query->where('siadat_records.name', 'LIKE', "%$q%")
                ->orWhere('siadat_records.family', 'LIKE', "%$q%")
                ->orWhere('siadat_records.level', 'LIKE', "%$q%");
        });
    }
}
