import {FormEvent, FunctionComponent, useEffect} from 'react';
import {Link} from 'react-router-dom';
import {useApiMethod} from '../../hooks/useApiMethod';
import {signInApiDeclaration} from '../../apiDeclarations';
import {Captions, pathnames} from '../../constants';
import {Field, Form} from '../../components/Form/Form';
import {PageLogo} from '../../components/PageLogo/PageLogo';

const fields: Field[] = [
    {
        name: 'username',
        placeholder: Captions.Email,
        autoComplete: 'username',
        required: true,
        error: 'User with email or username already exists',
    },
    {
        name: 'password',
        placeholder: Captions.Password,
        type: 'password',
        autoComplete: 'current-password',
        required: true,
        error: 'Password invalid',
    },
];

export const SignIn: FunctionComponent = () => {
    const {apiMethodState, fetchData} = useApiMethod<unknown, FormData>(signInApiDeclaration.login);
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
        fetchData(data);
    };

    return (
        <div>
            <PageLogo/>
            <Form
                fields={fields}
                loading={loading}
                error={error}
                submitCaption={Captions.SignIn}
                onSubmit={handleSignUp}
            >
                <Link to={pathnames.signUp}>{Captions.SignUpLink}</Link>
                <p>
                    <a href={"http://localhost:8080/oauth2/authorization/github"}>{Captions.SignInGitHub}</a>
                </p>
                <p>
                    <a href={"http://localhost:8080/oauth2/authorization/yandex"}>{Captions.SignInYandex}</a>
                </p>
            </Form>
        </div>
    );
};
