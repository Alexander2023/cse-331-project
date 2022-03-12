import {Component} from "react";

interface DropdownProps {
    buildings: Map<string, string>; // buildings to choose from
    label: string; // label of the dropdown
    defaultOption: string; // dropdown option to initially display
    onDropdownChange(option: string): void; // called when user interacts with dropdown
}

class Dropdown extends Component<DropdownProps, {}> {
    render() {
        let buildings: JSX.Element[] = [];
        for (let shortname of this.props.buildings.keys()) {
            buildings.push(<option key={buildings.length} value={shortname}>{this.props.buildings.get(shortname)}</option>);
        }

        return (
            <div>
                <label>
                    {this.props.label}
                    <select
                        value={this.props.defaultOption}
                        onChange={(e) => this.props.onDropdownChange(e.target.value)}>
                        {buildings}
                    </select>
                </label>
            </div>
        );
    }
}

export default Dropdown;