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

package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {
    private final Graph<Point, Double> campusMap;
    private final Map<String, CampusBuilding> campusBuildings;

    public CampusMap() { // +=+ Can a non-ADT have a constructor?
        campusMap = new Graph<>();
        campusBuildings = new HashMap<>();

        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.csv");

        for (CampusBuilding building : buildings) {
            campusBuildings.put(building.getShortName(), building);
        }

        for (CampusPath path : paths) {
            Point src = new Point(path.getX1(), path.getY1());
            Point dst = new Point(path.getX2(), path.getY2());

            campusMap.addNode(src);
            campusMap.addNode(dst);

            campusMap.addEdge(path.getDistance(), src, dst);
        }
    }

    @Override
    public boolean shortNameExists(String shortName) {
        return campusBuildings.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if (!shortNameExists(shortName)) {
            throw new IllegalArgumentException();
        }

        return campusBuildings.get(shortName).getLongName();
    }

    @Override
    public Map<String, String> buildingNames() {
        Map<String, String> names = new HashMap<>();

        for (String shortName : campusBuildings.keySet()) {
            names.put(shortName, campusBuildings.get(shortName).getLongName());
        }

        return names;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        if (startShortName == null || endShortName == null ||
            !shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            throw new IllegalArgumentException();
        }

        Point src = new Point(campusBuildings.get(startShortName).getX(),
                              campusBuildings.get(startShortName).getY());
        Point dst = new Point(campusBuildings.get(endShortName).getX(),
                              campusBuildings.get(endShortName).getY());

        return PathFinderPaths.findPath(campusMap, src, dst);
    }
}