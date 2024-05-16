import {FunctionComponent, useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useApiMethod} from "../../hooks/useApiMethod";
import {appsApiDeclaration} from "../../apiDeclarations";
import {ClientApp} from "../Clients/Clients";

export const ClientsEdit: FunctionComponent = () => {
    const {
        apiMethodState,
        fetchData
    } = useApiMethod<ClientApp, string>(appsApiDeclaration.getById);

    const {id} = useParams();

    useEffect(() => {
        if (!id) {
            return;
        }

        fetchData(id);
    }, [id, fetchData]);

    const {
        data
    } = apiMethodState;

    const clientContainer = (client: ClientApp) =>
        (
            <div>
                <div>id: {client.clientId}</div>
                <div>name: {client.clientName}</div>
                <div>expires: {new Date(client.issueTime).toLocaleString()}</div>
                <div>expires at: {new Date(client.expiresAt).toLocaleString()}</div>
                <div>redirect urls: {client.redirectUrls.join(",")}</div>
            </div>
        )

    return (
        <div>
            <h3>Client</h3>
            {data ? clientContainer(data) : (<></>)}
        </div>
    )
};
