import React, {Component} from "react";
import Dropdown from "./Dropdown";
import {BuildingMap} from "./types";

interface ControlsProps {
    pathStart: string;
    pathEnd: string;
    onStartDropdownChange(option: string): void;
    onEndDropdownChange(option: string): void;
    requestPath(): void;
}

interface ControlsState {
    buildings: BuildingMap;
}

class Controls extends Component<ControlsProps, ControlsState> {
    constructor(props: ControlsProps) { // +=+ check
        super(props);

        this.state = {
            buildings: {}
        }
    }

    componentDidMount() {
        this.requestBuildings();
    }


    requestBuildings = async () => {
        try {
            let response = await fetch("http://localhost:4567/buildings");

            if (!response.ok) {
                console.log("Wrong status!");
                return;
            }

            let json = await response.json();

            this.setState({
                buildings: json
            })
        } catch (e) {
            console.log(e);
        }
    }

    render() {
        return (
            <div id="controls">
                <Dropdown
                    buildings={this.state.buildings}
                    label={"Start:"}
                    defaultOption={this.props.pathStart}
                    onDropdownChange={this.props.onStartDropdownChange}
                />
                <Dropdown
                    buildings={this.state.buildings}
                    label={"End:"}
                    defaultOption={this.props.pathEnd}
                    onDropdownChange={this.props.onEndDropdownChange}
                />
                <button id="draw" onClick={() => {this.props.requestPath()}}>Directions</button>
            </div>
        );
    }
}

export default Controls;
