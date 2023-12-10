import React from 'react';
import './MainPanel.css';


const MainPanel = () => {
    const loremText = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vitae semper odio. Nulla facilisi. ' +
                       'Donec euismod, massa quis aliquam dapibus, turpis nunc ultricies diam, vitae ali';

    return (
        <div className="main-panel">
            {loremText}
            {/* This is where things will be displayed */}
        </div>
    );
}

export default MainPanel;