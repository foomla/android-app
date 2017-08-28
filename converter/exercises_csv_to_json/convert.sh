#!/bin/bash

function conv {
    echo "Convert $1 -> $2"
    go run src/org.foomla.converter/exercises_csv_to_json.go $1 tmp.json
    cat tmp.json | json_pp > $2
}

conv foomla_exercises.de.csv foomla_exercises.de.json
conv foomla_exercises.en.csv foomla_exercises.en.json
conv foomla_exercises.pro.de.csv foomla_exercises.pro.de.json
conv foomla_exercises.pro.en.csv foomla_exercises.pro.en.json

