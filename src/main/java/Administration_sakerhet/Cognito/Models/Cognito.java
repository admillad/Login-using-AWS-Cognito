package Administration_sakerhet.Cognito.Models;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cognito {
    public static User loggedInUser; //Objekt inloggad användare.
    public static String userPoolId = "eu-north-1_gGqNmE1cv"; //Kopplar Cognito med hjälp av UserPool Id:
    public static String poolClientId = "700886h048kt3t2gnb02pvg7b6";// Integrerar cognito med PoolClient Id.
    private static CognitoIdentityProviderClient cognitoIdentityProviderClient = getIdProviderClient(); // Kopplar Cognito med resultatet av metoden: getIdProviderClient().

    // Skaffa AWS Cognito identitetsleverantörsklient
    private static CognitoIdentityProviderClient getIdProviderClient() {
        var credentialsProvider = ProfileCredentialsProvider.create();
        CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(credentialsProvider)
                .build();
        return identityProviderClient;
    }

    // Initierar Cognitos "cognitoIdentityProviderClient".
    public static void InitIdp() {

        cognitoIdentityProviderClient = getIdProviderClient();

    }

    //Loggar ut användaren.
    public static void Logout() {
        loggedInUser = null;
    }

    //Registrerar en ny användare.
    public static boolean Register(String username, String password, String email) {

        signUp(cognitoIdentityProviderClient, poolClientId, username, password, email);
        return true;
    }

    //Skickar en "confirmaton code" till den nya användaren.
    public static boolean ConfirmUser(String username, String confirmationCode) {

        return confirmSignUp(cognitoIdentityProviderClient, poolClientId, confirmationCode, username);

    }

    //Autentiserar och loggar in användaren.
    public static boolean Login(String username, String password) {

        //Loggar in på cognito.
        InitiateAuthResponse authResponse = initiateAuth(cognitoIdentityProviderClient, poolClientId, username, password, userPoolId);

        //Om inloggningen lyckades loggar in.
        //Annars, stängs inloggningen av och man skickas inte vidare.
        if (authResponse != null) {
            String refreshToken = authResponse.authenticationResult().refreshToken();
            String accessToken = authResponse.authenticationResult().accessToken();
            User user = new User(username, refreshToken, accessToken);
            loggedInUser = user;
            return true;
        }

        return false;
    }

    //Metod för att ändra användarens lösenord.
    public static boolean ChangePassword(String oldPassword, String newPassword) {


        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder().previousPassword(oldPassword)
                .proposedPassword(newPassword).accessToken(loggedInUser.AccessToken).build();

        ChangePasswordResponse passwordResponse = cognitoIdentityProviderClient.changePassword(changePasswordRequest);

        if (passwordResponse != null) {
            return true;
        } else {
            return false;
        }

    }

    //Registrerar en ny användare.
    public static void signUp(CognitoIdentityProviderClient identityProviderClient, String clientId, String userName, String password, String email) {
        AttributeType userAttrs = AttributeType.builder()
                .name("email")
                .value(email)
                .build();

        List<AttributeType> userAttrsList = new ArrayList<>();
        userAttrsList.add(userAttrs);
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(userAttrsList)
                    .username(userName)
                    .clientId(clientId)
                    .password(password)
                    .build();

            identityProviderClient.signUp(signUpRequest);
            System.out.println("User has been signed up ");

        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    // bekräftar registreringen.
    public static boolean confirmSignUp(CognitoIdentityProviderClient identityProviderClient, String clientId, String code, String userName) {
        try {
            ConfirmSignUpRequest signUpRequest = ConfirmSignUpRequest.builder()
                    .clientId(clientId)
                    .confirmationCode(code)
                    .username(userName)
                    .build();

            identityProviderClient.confirmSignUp(signUpRequest);
            System.out.println(userName + " was confirmed");
            return true;

        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return false;
        }

    }

    // initiera autentiseringen och skriver token.
    public static InitiateAuthResponse initiateAuth(CognitoIdentityProviderClient identityProviderClient, String clientId, String userName, String password, String userPoolId) {
        try {
            Map<String, String> authParameters = new HashMap<>();
            authParameters.put("USERNAME", userName);
            authParameters.put("PASSWORD", password);

            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                    .clientId(clientId)
                    .authParameters(authParameters)
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .build();

            InitiateAuthResponse response = identityProviderClient.initiateAuth(authRequest);
            System.out.println("Result Challenge is : " + response.authenticationResult());
            System.out.println("Token = " + response.authenticationResult().accessToken());
            return response;

        } catch (CognitoIdentityProviderException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }

}

