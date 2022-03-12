import {Component} from "react";
import {BuildingMap} from "./types";

interface DropdownProps {
    buildings: BuildingMap; // buildings to choose from
    label: string; // label of the dropdown
    defaultOption: string; // dropdown option to initially display
    onDropdownChange(option: string): void; // called when user interacts with dropdown
}

class Dropdown extends Component<DropdownProps, {}> {
    render() {
        return (
            <div>
                <label>
                    {this.props.label}
                    <select
                        value={this.props.defaultOption}
                        onChange={(e) => this.props.onDropdownChange(e.target.value)}>
                        {Object.entries(this.props.buildings).map((names: [string, string], index: number) => {
                            return <option key={index} value={names[0]}>{names[1]}</option>
                        })}
                    </select>
                </label>
            </div>
        );
    }
}

export default Dropdown;