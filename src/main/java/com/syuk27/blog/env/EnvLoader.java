package com.syuk27.blog.env;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
	public static void loadEnv() {
        Dotenv dotenv = Dotenv.load();
        
        System.setProperty("SECURITY_USERNAME", dotenv.get("SECURITY_USERNAME"));
        System.setProperty("SECURITY_PASSWORD", dotenv.get("SECURITY_PASSWORD"));
        
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        
        System.setProperty("CLOUDINARY_CLOUD_NAME", dotenv.get("CLOUDINARY_CLOUD_NAME"));
        System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
        System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
        System.setProperty("CLOUDINARY_URL", dotenv.get("CLOUDINARY_URL"));
    }
}
