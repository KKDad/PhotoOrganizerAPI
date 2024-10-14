import React from 'react'
import ReactDOM from 'react-dom/client'
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import "./styles.css";
import ErrorPage from "./components/ErrorPage";
import Root, {
  loader as rootLoader,
  action as rootAction
} from "./routes/Root";
import Contact, {
  loader as contactLoader,
  action as contactAction
} from "./routes/Contact";
import EditContact, { action as editAction } from "./routes/EditContact";
import { action as destroyAction } from "./routes/DestroyContact";
import Index from "./routes/Index";


export const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <ErrorPage />,
    loader: rootLoader,
    action :rootAction,
    children: [
      {
        path: "contacts/:contactId",
        element: <Contact />,
        loader : contactLoader,
        action : contactAction,
      },
      {
        path: "contacts/:contactId/edit",
        element: <EditContact />,
        loader : contactLoader,
        action : editAction,
      },
      {
        path: "contacts/:contactId/destroy",
        action : destroyAction,
      },
      {
        path: "/",
        element: <Index />,
      },
    ],
  },
]);

// ===========================
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)