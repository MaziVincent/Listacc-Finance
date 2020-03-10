import { JwtHelperService } from '@auth0/angular-jwt';
import { DepartmentViewModel } from './department';

export class TokenUser {
    tokenString: string;
    currentUser: { person: { lastName: string, firstName: string, email: string }, phone: string };
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
            const decodedToken =  new JwtHelperService().decodeToken(userAndToken.tokenString);
            this.id = decodedToken.UserID;
            this.email = decodedToken.Email;
            this.role = decodedToken.role;
            this.fullName = userAndToken.currentUser.person.firstName + ' ' + userAndToken.currentUser.person.lastName;
        }
    }
}

export class UserViewModel {
    id: number;
    firstName: string;
    lastName: string;
    gender: string;
    userName: string;
    emailAddress: string;
    phone: string;
    role: string;
    address: string;
    status: boolean | string;
    department: DepartmentViewModel;
    departmentId: string;
}

export class StatusOption {
    name: string;
    value: string;
    iconClass: string;
}
