export interface BuildingMap {
    [key: string]: string; // map from short names to long names of buildings
}

export interface Edge {
    x1: number; // x coordinate of start point
    y1: number; // y coordinate of start point
    x2: number; // x coordinate of end point
    y2: number; // y coordinate of end point
}