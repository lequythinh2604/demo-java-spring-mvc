package com.example.demo.controller.admin;

import com.example.demo.domain.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.UploadService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> products = productService.handleFindAll();
        model.addAttribute("products", products);
        return "admin/product/index";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProduct(@ModelAttribute("newProduct") @Valid Product product,
                                BindingResult bindingResult,
                                @RequestParam("imgFile") MultipartFile file
    ) {
        // log errors
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            System.out.println(">>> " + fieldError.getField() + " - " + fieldError.getDefaultMessage());
        }

        // check error
        if (bindingResult.hasErrors()) {
            return "admin/product/create";
        }

        String productImage = uploadService.handleSaveUploadFile(file, "product");
        product.setImage(productImage);

        productService.handleSave(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable("id") Long id) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @GetMapping("/admin/product/detail/{id}")
    public String getProductDetailPage(Model model, @PathVariable("id") Long id) {


        return "admin/product/detail";
    }
}
