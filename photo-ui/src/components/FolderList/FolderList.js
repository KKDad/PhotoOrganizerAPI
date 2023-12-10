import React, { useState, useEffect } from 'react';

const FolderList = () => {
    const [folders, setFolders] = useState([]);

    useEffect(() => {
        fetch('https://api.example.com/folders') // replace with your API endpoint
            .then(response => response.json())
            .then(data => setFolders(data));
    }, []);

    return (
        <div className="folder-list">
            <ul>
                {folders.map((folder, index) => (
                    <li key={index}>{folder}</li>
                ))}
            </ul>
        </div>
    );
};

export default FolderList;