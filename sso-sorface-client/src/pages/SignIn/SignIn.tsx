import { FormEvent, FunctionComponent, Fragment, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useApiMethod } from '../../hooks/useApiMethod';
import { SignInBody, usersApiDeclaration } from '../../apiDeclarations';
import { Captions, pathnames } from '../../constants';

const fields = [
  {
    name: 'username',
    placeholder: Captions.Login,
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
  },
];

export const SignIn: FunctionComponent = () => {
  const { apiMethodState, fetchData } = useApiMethod<unknown, SignInBody>(usersApiDeclaration.registry);
  const { process: { loading, error }, data } = apiMethodState;

  useEffect(() => {
    if (!data) {
      return;
    }
    alert(`Response data: ${JSON.stringify(data)}`)
  }, [data]);

  const handleSignIn = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const data = new FormData(form);
    const formDataObj: Record<string, FormDataEntryValue> = {};
    data.forEach((value, key) => (formDataObj[key] = value));
    fetchData(formDataObj);
  };

  return (
    <div>
      <h2><Link to={pathnames.home}>{Captions.BackSymbol}</Link> {Captions.SignIn}</h2>
      <form onSubmit={handleSignIn}>
        {fields.map(field => (
          <Fragment key={field.name}>
            <label htmlFor={field.name}><p>{field.placeholder}:</p></label>
            <input id={field.name} {...field} />
          </Fragment>
        ))}
        <p>
          <input type="submit" value={Captions.SignIn} />
        </p>
        <p>
          {loading && <div>{Captions.Loading}...</div>}
          {error && <div>{Captions.Error}: {error}</div>}
        </p>
      </form>
    </div>
  );
};
