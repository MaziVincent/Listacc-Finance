import { JwtHelperService } from '@auth0/angular-jwt';

export class TokenUser {
    token: string;
    user: { lastName: string, firstName: string, email: string, phone: string};
}

export class LoginUser {
    constructor() {}

    emailAddress: string;
    password: string;
}

export class MyUser {
    id: string;
    email: string;
    role: string;
    fullName: string;

    constructor(userAndToken: TokenUser) {
        if (userAndToken != null) {
            const decodedToken =  new JwtHelperService().decodeToken(userAndToken.token);
            this.id = decodedToken.nameid;
            this.email = decodedToken.unique_name;
            this.role = decodedToken.role;
            this.fullName = userAndToken.user.firstName + ' ' + userAndToken.user.lastName;
        }
    }
}

export class UserViewModel{
    id: number;
    firstName: string;
    lastName: string;
    gender: string;
    userName: string;
    email: string;
    phoneNumber: string;
    role: string;
    address: string;
    deactivated: boolean | string;
}
