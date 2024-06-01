import { FunctionComponent } from 'react';
import { Field, Form } from "../../components/Form/Form";
import { useApiMethodCsrf } from '../../hooks/useApiMethodCsrf';
import { CreateAppBody, appsApiDeclaration } from '../../apiDeclarations';
import { Captions } from '../../constants';

const nameField = 'name';
const redirectionUrlsField = 'redirectionUrls';

const fields: Field[] = [
    {
        name: nameField,
        placeholder: Captions.ClientName,
    },
    {
        name: redirectionUrlsField,
        placeholder: Captions.RedirectionUrls,
    },
];

export const ClientsAdd: FunctionComponent = () => {
    const { fetchData } = useApiMethodCsrf<unknown, CreateAppBody>(appsApiDeclaration.create);

    const handleSubmit = (formData: FormData) => {
        const data = {
            [nameField]: String(formData.get(nameField)),
            [redirectionUrlsField]: String(formData.get(redirectionUrlsField)),
        };
        fetchData(data);
    };

    return (
        <div>
            <h3>{Captions.AddClient}</h3>
            <Form
                fields={fields}
                fieldErrors={{}}
                submitCaption={Captions.AddClient}
                onSubmit={handleSubmit}
            />
        </div>
    );
};
