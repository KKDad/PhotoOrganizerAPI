import React from 'react';
import Header from './components/Header/Header';
import FolderList from './components/FolderList/FolderList';
import MainPanel from './components/MainPanel/MainPanel';

function App() {
  return (
    <div className="App">
      <Header />
      <div className="container">
        <FolderList />
        <MainPanel />
      </div>
    </div>
  );
}

export default App;