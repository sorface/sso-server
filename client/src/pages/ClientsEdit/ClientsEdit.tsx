import { FunctionComponent, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useApiMethod } from "../../hooks/useApiMethod";
import { appsApiDeclaration } from "../../apiDeclarations";
import { ClientApp } from "../Clients/Clients";
import { Captions } from '../../constants';
import { Field, Form } from '../../components/Form/Form';
import { Loader } from '../../components/Loader/Loader';
import { RenewSecret } from './components/RenewSecret/RenewSecret';

import './ClientsEdit.css';

export const ClientsEdit: FunctionComponent = () => {
    const {
        apiMethodState,
        fetchData
    } = useApiMethod<ClientApp, string>(appsApiDeclaration.getById);

    const {
        data,
        process: { loading, error },
    } = apiMethodState;

    const { id } = useParams();

    const fields: Field[] = [
        {
            name: 'id',
            placeholder: Captions.Id,
            readOnly: true,
            defaultValue: data?.clientId,
        },
        {
            name: 'name',
            placeholder: Captions.ClientName,
            defaultValue: data?.clientName,
        },
        {
            name: 'redirectUrls',
            placeholder: Captions.RedirectionUrls,
            defaultValue: data?.redirectUrls.join(","),
        },
        {
            name: 'issueTime',
            placeholder: Captions.IssueTime,
            readOnly: true,
            defaultValue: data?.issueTime && new Date(data.issueTime).toLocaleString(),
        },
        {
            name: 'expiresAt',
            placeholder: Captions.ExpiresAt,
            readOnly: true,
            defaultValue: data?.expiresAt && new Date(data.expiresAt).toLocaleString(),
        },
    ];

    useEffect(() => {
        if (!id) {
            return;
        }

        fetchData(id);
    }, [id, fetchData]);

    return (
        <div className='client-edit'>
            <h3>{Captions.EditClient}</h3>
            <div className='client-edit-body'>
                {loading && <Loader />}
                {!!error && <div>{Captions.Error}: {error}</div>}
                {!!data && (
                        <Form
                            styled
                            fields={fields}
                            fieldErrors={{}}
                            submitCaption={Captions.Save}
                        >
                            <RenewSecret />
                        </Form>
                )}
            </div>
        </div>
    )
};
