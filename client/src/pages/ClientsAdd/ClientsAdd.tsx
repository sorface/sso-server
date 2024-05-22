import {FunctionComponent} from 'react';
import {Field, Form} from "../../components/Form/Form";
import { ApiEndpoint } from '../../types/apiContracts';

export const ClientsAdd: FunctionComponent = () => {
    const fields: Field[] = [
        {
            name: 'name',
            placeholder: 'Client Name'
        },
        {
            name: 'redirectionUrls',
            placeholder: 'Redirection Urls'
        }
    ];

    return (
        <div>
            <h3>Client Add</h3>
            <Form fields={fields} fieldErrors={{}} htmlAction={ApiEndpoint.Apps} htmlMethod={'POST'}/>
        </div>
    )
};
