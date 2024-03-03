import {FormEvent, FunctionComponent, useEffect} from 'react';
import {Link} from 'react-router-dom';
import {useApiMethod} from '../../hooks/useApiMethod';
import {SignUpBody, usersApiDeclaration} from '../../apiDeclarations';
import {Captions, pathnames} from '../../constants';
import {Field, Form} from '../../components/Form/Form';

const fields: Field[] = [
    {
        name: 'username',
        placeholder: Captions.Signin,
        autoComplete: 'username',
        required: true,
    },
    {
        name: 'password',
        placeholder: Captions.Password,
        type: 'password',
        autoComplete: 'current-password',
        required: true,
    },
    {
        name: 'email',
        placeholder: Captions.Email,
        required: true,
    },
    {
        name: 'firstName',
        placeholder: Captions.FirstName,
        required: true,
    },
    {
        name: 'lastName',
        placeholder: Captions.LastName,
        required: true,
        error: 'test error',
    },
];

export const SignUp: FunctionComponent = () => {
    const {apiMethodState, fetchData} = useApiMethod<unknown, SignUpBody>(usersApiDeclaration.registry);
    const {process: {loading, error}, data} = apiMethodState;

    useEffect(() => {
        if (!data) {
            return;
        }
        alert(`Response data: ${JSON.stringify(data)}`)
    }, [data]);

    const handleSignUp = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const form = event.target as HTMLFormElement;
        const data = new FormData(form);
        const formDataObj: Record<string, FormDataEntryValue> = {};
        data.forEach((value, key) => (formDataObj[key] = value));
        fetchData(formDataObj);
    };

    return (
        <div>
            <Form
                fields={fields}
                loading={loading}
                error={error}
                submitCaption={Captions.SignUp}
                onSubmit={handleSignUp}
            >
                <Link to={pathnames.signIn}>{Captions.SignInLink}</Link>
            </Form>
        </div>
    );
};
