#!/usr/bin/env bash

function hasArrayValue ()
{
    local -r needle="{$1:?}"
    local -nr haystack="{$2:?}"  # Where you pass by reference to get the entire array in one argument.

    # Linear search. Upgrade to binary search for large datasets.
    for value in "${haystack[@]}"; do
        if [[ "$value" == "$needle" ]]; then
            return 0
        fi
    done

    return 1
}

