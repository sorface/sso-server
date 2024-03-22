import React, {FunctionComponent, useEffect} from 'react';
import './Session.css';
import {useApiMethod} from "../../hooks/useApiMethod";
import {accountsApiDeclaration} from "../../apiDeclarations";

export interface Session {

    id: string;

    device?: string;

    deviceBrand?: string;

    deviceType?: string;

    browser?: string;

    active: boolean;

    createdAt: number;

}

export interface SessionContext {

    sessions: Session[];

}

export const Session: FunctionComponent = () => {
    const {
        apiMethodState,
        fetchData
    } = useApiMethod<SessionContext, undefined>(accountsApiDeclaration.getCurrentSession);

    const {
        process: {
            loading,
            error
        },
        data
    } = apiMethodState;

    useEffect(() => {
        fetchData(undefined);
    }, []);

    useEffect(() => {
        if (!data) {
            return;
        }
    }, [data]);

    return <div>
        {loading ? (<p>Loading</p>) : (<table className="table">
            <thead>
            <tr>
                <th>Id</th>
                <th>Device</th>
                <th>Device Type</th>
                <th>Device Brand</th>
                <th>Interface</th>
                <th>Created At</th>
            </tr>
            </thead>
            <tbody>
            {data?.sessions.map((session) => (
                <tr className={session.active ? 'current_session' : ''} key={session.id}>
                    <td>{session.id}</td>
                    <td>{session.device}</td>
                    <td>{session.deviceType}</td>
                    <td>{session.deviceBrand}</td>
                    <td>{session.browser}</td>
                    <td>{new Date(session.createdAt).toLocaleString()}</td>
                </tr>
            ))}
            </tbody>
        </table>)
        }
    </div>;
}