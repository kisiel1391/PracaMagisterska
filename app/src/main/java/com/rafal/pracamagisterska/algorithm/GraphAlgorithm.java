package com.rafal.pracamagisterska.algorithm;

import com.rafal.pracamagisterska.objects.Path;

/**
 * Created by Rafal on 2017-06-05.
 */

public interface GraphAlgorithm {

    //Type of heuristic
    int EUCLIDEAN_HEURISTIC = 0;
    int MANHATTAN_HEURISTIC = 1;

    //Type of cost attribute
    int DISTANCE = 0;
    int TRAVELTIME = 1;

    //Type of edge weigth multiplier
    int NONE = 0;
    int WEIGTH = 1;

    Path findPath(String startNodeId, String endNodeId, int costAttr, int weightAttr);

}
