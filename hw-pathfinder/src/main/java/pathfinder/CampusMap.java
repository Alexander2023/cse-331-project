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

/**
 * CampusMap is an immutable model of buildings around UW campus.
 * Buildings in CampusMap have unique short names.
 */
public class CampusMap implements ModelAPI {
    public static final boolean DEBUG = false;

    private final Graph<Point, Double> campusMap;
    private final Map<String, CampusBuilding> campusBuildings;

    // Abstraction Function:
    // A graph of points as coordinates of paths leading to buildings and
    // doubles as distances between points. Points of buildings are amongst
    // these points, but use a map of key/value pairs where keys are short
    // names of buildings and values are campus buildings to identify them
    // with their point in the graph

    // Rep Invariant:
    // campusMap != null &&
    // campusBuildings != null &&
    // campusMap doesn't contain null points
    // campusMap doesn't contain null distances
    // campusBuildings doesn't contain null short names
    // campusBuildings doesn't contain null campus buildings
    // campusBuildings doesn't contain duplicate short names

    /**
     * Checks that the rep is maintained
     */
    private void checkRep() {
        assert campusMap != null;
        assert campusBuildings != null;

        if (DEBUG) {
            for (Point point : campusMap) {
                assert point != null;
            }

            for (String shortName : campusBuildings.keySet()) {
                assert shortName != null;

                assert campusBuildings.get(shortName) != null;
            }
        }
    }

    /**
     * @spec.effects Constructs a new CampusMap
     */
    public CampusMap() {
        campusMap = new Graph<>();
        campusBuildings = new HashMap<>();

        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.csv");

        // Inv: campusBuildings contains all buildings from 0 to i-1
        for (CampusBuilding building : buildings) {
            campusBuildings.put(building.getShortName(), building);
        }

        // Inv: campusMap contains all points of coordinates and
        // edges between points of all paths from 0 to i-1
        for (CampusPath path : paths) {
            Point src = new Point(path.getX1(), path.getY1());
            Point dst = new Point(path.getX2(), path.getY2());

            campusMap.addNode(src);
            campusMap.addNode(dst);

            campusMap.addEdge(path.getDistance(), src, dst);
        }

        checkRep();
    }

    @Override
    public boolean shortNameExists(String shortName) {
        checkRep();

        boolean result = campusBuildings.containsKey(shortName);

        checkRep();

        return result;
    }

    @Override
    public String longNameForShort(String shortName) {
        checkRep();

        if (!shortNameExists(shortName)) {
            checkRep();
            throw new IllegalArgumentException();
        }

        String longName = campusBuildings.get(shortName).getLongName();

        checkRep();

        return longName;
    }

    @Override
    public Map<String, String> buildingNames() {
        checkRep();

        Map<String, String> names = new HashMap<>();

        // Inv: names contains all shortNames/longName pairs
        // of all shortNames from 0 to i-1
        for (String shortName : campusBuildings.keySet()) {
            names.put(shortName, campusBuildings.get(shortName).getLongName());
        }

        checkRep();

        return names;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        checkRep();

        if (startShortName == null || endShortName == null ||
            !shortNameExists(startShortName) || !shortNameExists(endShortName)) {
            checkRep();
            throw new IllegalArgumentException();
        }

        Point src = new Point(campusBuildings.get(startShortName).getX(),
                              campusBuildings.get(startShortName).getY());
        Point dst = new Point(campusBuildings.get(endShortName).getX(),
                              campusBuildings.get(endShortName).getY());

        Path<Point> path = PathFinderPaths.findPath(campusMap, src, dst);

        checkRep();

        return path;
    }
}