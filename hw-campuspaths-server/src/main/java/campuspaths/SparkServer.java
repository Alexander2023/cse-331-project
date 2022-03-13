/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.Map;

/**
 * SparkServer runs a server for sharing building and path finding information
 * of the UW Seattle campus
 */
public class SparkServer {

    // This class does not represent an ADT

    private static CampusMap map;

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        map = new CampusMap();

        // Endpoint which returns a JSON representation of a mapping of short names
        // to long names of the buildings
        Spark.get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Map<String, String> names = map.buildingNames();

                Gson gson = new Gson();
                return gson.toJson(names);
            }
        });

        // Endpoint which accepts query parameters for the start and end short building
        // names and returns a JSON representation of the corresponding path
        // Halts with special status 400 if either start or end query parameters are null
        Spark.get("/path", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String startShortName = request.queryParams("start");
                String endShortName = request.queryParams("end");

                if (startShortName == null || endShortName == null) {
                    Spark.halt(400, "must provide a start and end");
                }

                Path<Point> path = map.findShortestPath(startShortName, endShortName);

                Gson gson = new Gson();
                return gson.toJson(path);
            }
        });
    }

}
