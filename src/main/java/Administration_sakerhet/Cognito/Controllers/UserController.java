package Administration_sakerhet.Cognito.Controllers;

import Administration_sakerhet.Cognito.Models.Cognito;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @GetMapping("/")
    // Om användaren inte är inloggad, omdirigerar den till registreringssidan.
    // Annars så lägger metoden till den inloggade användarens och returnera "hem"-vyn.
    public String GetHome(Model model) {

        if (Cognito.loggedInUser == null) {
            return "redirect:/register";
        } else {
            model.addAttribute("username", Cognito.loggedInUser.username);
            return "home";
        }
    }

    @GetMapping("/register")
    // Visar registration sidan.
    public String GetRegister() {

        return "registration";
    }

    @PostMapping("/register")
    // Hanterae registreringen av en användare.
    // Registrerar användaren med det angivna användarnamnet, e-postadressen och lösenordet.
    // Om registreringen lyckades, omdirigerar den till verifieringssidan.
    // Annars, returnerar den till registreringssidan.
    public String PostRegister(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               RedirectAttributes redirectAttributes) {

        System.out.println(username);
        System.out.println(email);
        System.out.println(password);

        if (Cognito.Register(username, password, email)) {

            redirectAttributes.addAttribute("email", email);
            redirectAttributes.addAttribute("username", username);
            return "redirect:/verify";

        } else {
            return "registration";
        }
    }

    @GetMapping("/verify")
    // Visar verifieringssidan.
    // Lägger till e-postadressen och användarnamnet i cognito.
    public String GetVerify(@RequestParam("email") String email,
                            @RequestParam("username") String username,
                            Model model
    ) {

        model.addAttribute("email", email);
        model.addAttribute("username", username);
        return "verify";
    }

    @PostMapping("/verify")
    // Hanterar verifieringen.
    // Bekräftar användaren med det nämna användarnamnet och token.
    // Om verifieringen lyckas, omdirigeras användaren till inloggningssidan.
    // Annars, returneras användaren till verifieringssidan.
    public String PostVerify(@RequestParam("username") String username,
                             @RequestParam("confirmationCode") String confirmationCode
    ) {

        if (Cognito.ConfirmUser(username, confirmationCode)) {

            return "redirect:/login";

        } else {
            return "verify";
        }
    }


    @GetMapping("/login")
    //"displays" inloggningssidan.
    public String GetLogin() {

        return "login";
    }

    @PostMapping("/login")
    // Hanterar inloggningen av användare.
    // Autentiserar användaren med det inskrivna användarnamnet och lösenordet.
    // Om inloggningen lyckas, omdirigerar metoden till startsidan.
    // Annars, returneras inloggningssidan.
    public String PostLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            RedirectAttributes redirectAttributes) {

        System.out.println(username);
        System.out.println(password);

        if (Cognito.Login(username, password)) {

            return "redirect:/";

        } else {
            return "login";
        }
    }

    @PostMapping("/logout")
    // Hanterar utloggningen av användare.
    // Loggar ut användaren och omdirigera till inloggningssidan.
    public String PostLogout() {
        Cognito.Logout();
        return "redirect:/login";
    }

    @PostMapping("changePassword")
    // Hanterar funktionen för att ändra lösenord.
    // Ändrar användarens lösenord från det gamla lösenordet till det nya lösenordet.
    // Om det går att ändra lösenordet, omdirigeras användaren till hemsidan.
    // Annars, returneras "home"-sidan.

    public String PostChangePassword(@RequestParam("oldPassword") String oldPassword,
                                     @RequestParam("newPassword") String newPassword
    ) {

        System.out.println("Changed password from " + oldPassword + " to " + newPassword);

        if (Cognito.ChangePassword(oldPassword, newPassword)) {
            return "redirect:/";
        } else {
            return "home";

        }
    }

}
