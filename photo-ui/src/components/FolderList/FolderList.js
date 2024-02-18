import React, { useState, useEffect } from 'react';


const FolderList = () => {
    const [folders, setFolders] = useState([]);

    // useEffect(() => {
    //     fetch('https://api.example.com/folders') // replace with your API endpoint
    //         .then(response => response.json())
    //         .then(data => setFolders(data));
    // }, []);

    useEffect(() => {
        const staticFolders = [
            { id: 1, name: 'Folder 1' },
            { id: 2, name: 'Folder 2' },
            { id: 3, name: 'Folder 3' }
        ];
        setFolders(staticFolders);
    }, []);

    return (
        <div className="folder-list">
            <ul>
                {folders.map((folder) => (
                    <li key={folder.id}>{folder.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default FolderList;