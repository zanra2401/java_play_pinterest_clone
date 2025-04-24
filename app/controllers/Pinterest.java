package controllers;

import play.*;
import play.mvc.*;
import play.db.Database;
import play.libs.Files.TemporaryFile;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Http;
import models.Image;
import models.User;



import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import models.Image;
import models.Image.ImageData;
import models.Image.ImageDetail;


public class Pinterest extends Controller {

    private final Database db;
    private final Image imageModel;
    private final User userModel;

    // Inject Database
    @Inject
    public Pinterest(Database db) {
        this.db = db;
        this.userModel = new User(db);
        this.imageModel = new Image(db);
    }

    public Result index(Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);

        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }
        
        List<ImageData> images = imageModel.getAllImages();

        return ok(views.html.index.render(images));
    }

    public Result profile(Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        String username = userModel.getUsername(Long.parseLong(userId));
        String pp = userModel.getPPById(Long.parseLong(userId));
        List<ImageData> images = imageModel.getImageById(Long.parseLong(userId));
        return ok(views.html.profile.render(images, username, pp));
    }

    public Result loginFe(Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId != null)
        {
            return redirect(routes.Pinterest.index());
        }

        return ok(views.html.login.render());
    }

    public Result daftarFe() {
        return ok(views.html.daftar.render());
    }

    public Result uploadGambar(Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        // Get the multipart data from the request
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> image = body.getFile("image");

        // Check if file was uploaded
        if (image != null) {
            String originalFilename = image.getFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String randomFileName = UUID.randomUUID().toString() + extension;
            TemporaryFile tempFile = image.getRef();

            // Specify the destination path where the file will be stored
            String filePath = "public/uploads/" + randomFileName;
            File dest = new File(filePath);

            try {
                // Save the uploaded file to the server
                tempFile.copyTo(dest, true);

                // Store file information into the database
                boolean uploaded = imageModel.uploadImage(Long.parseLong(userId), filePath.replaceFirst("^public/", ""));

                if (uploaded) {
                    return redirect(routes.Pinterest.profile());
                } else {
                    return internalServerError("Error while saving image information to database.");
                }

            } catch (Exception e) {  // Catching general exceptions if necessary
                e.printStackTrace();
                return internalServerError("Error while uploading the file.");
            }

        } else {
            return badRequest("Missing file.");
        }
    }

    public Result login(Http.Request request) {

        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId != null)
        {
            return redirect(routes.Pinterest.index());
        }
   
        String username = request.body().asFormUrlEncoded().get("username")[0];
        String password = request.body().asFormUrlEncoded().get("password")[0];


        Long loggedIn = (long) userModel.login(username, password);
        if (loggedIn != 0) {
            return redirect(routes.Pinterest.index()).addingToSession(request, "id", Long.toString(loggedIn));
        } else {
            return unauthorized("Login gagal");
        }
    }

    public Result logout(Http.Request request) {
        return redirect("/").removingFromSession(request, "id");
    }


    public Result deleteGambar(Http.Request request)
    {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        String id = request.body().asFormUrlEncoded().get("id")[0];

        imageModel.deleteImage(Long.parseLong(id));

        return redirect(routes.Pinterest.profile());
    }

    public Result detailGambar(Long id, Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        Image.ImageDetail imageDetail = imageModel.getImageDetailById(id);
        boolean telahLike = userModel.telahLike(imageDetail.getImageId(), Long.parseLong(userId));
        boolean sudahKomen = userModel.sudahKomen(imageDetail.getImageId(), Long.parseLong(userId));
        String pp = userModel.getPPById(Long.parseLong(userId));

        System.err.println(telahLike);

        if (imageDetail != null) {
            return ok(views.html.detail.render(imageDetail, telahLike, pp, sudahKomen));
        } else {
            return notFound("Image not found");
        }
    }

    public Result likeImage(Long imageId, Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        boolean success = imageModel.addLike(Long.parseLong(userId), imageId);
        if (success) {
            return redirect(routes.Pinterest.detailGambar(imageId));
        } else {
            return internalServerError("Failed to like image");
        }
    }

    
    public Result unlike(Long imageId, Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        boolean success = imageModel.unLike(Long.parseLong(userId), imageId);

        if (success) {
            return redirect(routes.Pinterest.detailGambar(imageId));
        } else {
            return internalServerError("Failed to like image");
        }
    }
    
    public Result editNama(Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        String[] namaBaruArr = formData.get("namaBaru");

        if (namaBaruArr != null && namaBaruArr.length > 0) {
            String namaBaru = namaBaruArr[0];

            boolean sukses = userModel.updateUsername(Long.parseLong(userId), namaBaru);

            if (sukses) {
                return redirect("/profile").flashing("success", "Nama berhasil diubah!");
            } else {
                return redirect("/profile").flashing("error", "Gagal mengubah nama.");
            }
        }

        return redirect("/profile").flashing("error", "Nama tidak valid.");
    }

    public Result daftar(Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        String username = formData.get("username")[0];
        String password = formData.get("password")[0];

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return redirect("/register").flashing("error", "Username dan password tidak boleh kosong.");
        }

        // Cek apakah username sudah terdaftar
        if (userModel.isUsernameExist(username)) {
            return redirect("/register").flashing("error", "Username sudah digunakan.");
        }

        boolean sukses = userModel.register(username, password);

        if (sukses) {
            return redirect("/login").flashing("success", "Pendaftaran berhasil. Silakan login.");
        } else {
            return redirect("/register").flashing("error", "Pendaftaran gagal. Coba lagi.");
        }
    }

    public Result editFoto(Http.Request request) {
        String userId = request.session().getOptional("id").orElse(null);

        if (userId == null) {
            return redirect(routes.Pinterest.loginFe());  // Redirect ke halaman login jika session tidak ada
        }

        // Mendapatkan data multipart dari request
        Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
        Http.MultipartFormData.FilePart<TemporaryFile> fotoBaru = body.getFile("fotoBaru");

        // Memeriksa apakah file di-upload
        if (fotoBaru != null) {
            String originalFilename = fotoBaru.getFilename();  // Nama file asli
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String randomFileName = UUID.randomUUID().toString() + extension;  // Membuat nama file acak
            TemporaryFile tempFile = fotoBaru.getRef();  // File sementara yang di-upload

            // Menentukan path tujuan tempat file akan disimpan
            String filePath = "public/pp/" + randomFileName;
            File dest = new File(filePath);  // Destinasi file yang disimpan di server

            try {
                // Menyimpan file yang di-upload ke server
                tempFile.copyTo(dest, true);  // Meng-copy file dari TemporaryFile ke destinasi

                // Update path foto profil di database
                boolean updated = userModel.updateProfilePicture(Long.parseLong(userId), filePath.replaceFirst("^public/", ""));

                if (updated) {
                    return redirect(routes.Pinterest.profile());  // Redirect ke profil setelah berhasil update PP
                } else {
                    return internalServerError("Error while updating profile picture in database.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return internalServerError("Error while uploading the profile picture.");
            }

        } else {
            return badRequest("Missing file.");
        }
    }

    public Result komen(Http.Request request )
    {
        String userId = request.session().getOptional("id").orElse(null);
        
        if (userId == null)
        {
            return redirect(routes.Pinterest.loginFe());
        }

        String imageId = request.body().asFormUrlEncoded().get("imageId")[0];
        String content = request.body().asFormUrlEncoded().get("content")[0];

        boolean success = imageModel.addComment(Long.parseLong(userId), Long.parseLong(imageId), content);
        
        if (success) {
            return redirect(routes.Pinterest.detailGambar(Long.parseLong(imageId)));
        } else {
            return internalServerError("Failed to like image");
        } 
    }

}
