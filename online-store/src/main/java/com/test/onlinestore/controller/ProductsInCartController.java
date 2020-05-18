package com.test.onlinestore.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.test.onlinestore.entity.Cart;
import com.test.onlinestore.entity.Product;
import com.test.onlinestore.entity.ProductsInCart;
import com.test.onlinestore.exception.ElementAlreadyExistException;
import com.test.onlinestore.exception.RecordNotFoundException;
import com.test.onlinestore.repository.CartRepository;
import com.test.onlinestore.repository.ProductRepository;
import com.test.onlinestore.repository.ProductsInCartRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Api(tags = {"productsincart"})
@RestController
@RequestMapping("/productsincart")
@AllArgsConstructor
public class ProductsInCartController {

    private final ProductsInCartRepository productsInCartRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @ApiOperation(value = "Find all products in all carts", response = ProductsInCart.class)
    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json")
    public List<ProductsInCart> getProductsInAllCarts(Model model) {
        model.addAttribute("method", "post");
        return productsInCartRepository.findAll();
    }

    @ApiOperation(value = "Find product in a cart by id", response = ProductsInCart.class)
    @RequestMapping(value = "/{cartId}/{productId}", method = RequestMethod.GET, produces = "application/json")
    public List<ProductsInCart> getProductById(@PathVariable("cartId") Long cartId, @PathVariable("productId")
            Long productId, Model model)
            throws RecordNotFoundException {
        List<ProductsInCart> productsInCart = productsInCartRepository.findAll()
                .stream()
                .filter(p -> p.getCart().getId().equals(cartId) & p.getProduct().getId().equals(productId))
                .collect(Collectors.toList()
                );

        model.addAttribute("productsInCart", productsInCart);

        return productsInCart;
    }

    @ApiOperation(value = "Get all products in a cart", response = ProductsInCart.class)
    @RequestMapping(value = "/{id}/all", method = RequestMethod.GET, produces = "application/json")
    public List<ProductsInCart> getAllProductsInCart(@PathVariable("id") Long id, Model model)
            throws RecordNotFoundException {

        model.addAttribute("method", "get");

        return productsInCartRepository.findAll()
                .stream()
                .filter(p -> p.getCart().getId().equals(id))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Add new product in the cart", response = ProductsInCart.class)
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public String addToCart(@RequestParam Long cartId, @RequestParam Long productId, @RequestParam int quantity,
                            Model model) throws ElementAlreadyExistException {

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return "Product doesn't exist";
        }

        Cart cart = cartRepository.findCartById(cartId);
        if (cart == null) {
            return "Cart doesn't exist";
        }

        Long detectedId = productsInCartRepository.findAll()
                .stream()
                .filter(p -> p.getCart().getId().equals(cartId) & p.getProduct().getId().equals(productId))
                .findAny().orElseThrow()
                .getProduct()
                .getId();

        if (detectedId.equals(productId)) {
            return "Such product already exist in a cart";
        }

        ProductsInCart productsInCart = new ProductsInCart();
        productsInCart.setCart(cart);
        productsInCart.setProduct(product);
        productsInCart.setQuantity(quantity);
        productsInCartRepository.save(productsInCart);
        Iterable<ProductsInCart> productsInCarts = productsInCartRepository.findAll();
        model.addAttribute("productsInCart", productsInCarts);
        return "Successful";
    }

    @ApiOperation(value = "Delete product from a cart", response = ProductsInCart.class)
    @RequestMapping(value = "{cartId}/delete/{productId}", method = RequestMethod.DELETE, produces = "application/json")
    public boolean deleteProduct(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId,
                                 Model model)
            throws RecordNotFoundException {

        productsInCartRepository.deleteById(
                productsInCartRepository.findAll().stream()
                        .filter(p -> p.getProduct().getId().equals(productId) & p.getCart().getId().equals(cartId))
                        .findAny().orElseThrow()
                        .getId()
        );

        model.addAttribute("method", "post");
        return true;
    }

    @ApiOperation(value = "Get total price for a cart", response = ProductsInCart.class)
    @RequestMapping(value = "/{id}/totalprice", method = RequestMethod.GET, produces = "application/json")
    public Double getTotalPrice(@PathVariable("id") Long id, Model model) throws RecordNotFoundException {
        List<Double> cost = new ArrayList<>();
        List<ProductsInCart> productsInCart = getAllProductsInCart(id, model);

        for (ProductsInCart tmp : productsInCart) {
            cost.add(tmp.getProduct().getPrice() * (double) tmp.getQuantity());
        }

        Double totalPrice = cost.stream()
                .mapToDouble(c -> c)
                .sum();

        model.addAttribute("totalPrice", totalPrice);
        return totalPrice;
    }

    @ApiOperation(value = "Form a receipt", response = ProductsInCart.class)
    @RequestMapping(value = "/{id}/receipt", method = RequestMethod.GET, produces = "application/json")
    public List<String> formReceipt(@PathVariable("id") Long id, Model model) throws RecordNotFoundException {
        List<String> receipt = new ArrayList<>();
        Double totalPrice = getTotalPrice(id, model);
        String dateStamp = simpleDateFormat.format(System.currentTimeMillis());
        String fName = "src/main/resources/receipt_" + dateStamp + ".pdf";

        List<ProductsInCart> productsInCart = getAllProductsInCart(id, model);
        receipt.add(" No  Product name  Price   Quantity ");
        for (int i = 0; i < productsInCart.size(); i++) {
            receipt.add((i + 1) + ". " + productsInCart.get(i).getProduct().getName() + " "
                    + productsInCart.get(i).getProduct().getEan() + " "
                    + productsInCart.get(i).getProduct().getPrice().toString() + " x "
                    + productsInCart.get(i).getQuantity());
        }
        receipt.add("Total price: " + totalPrice);

        Document document = new Document();

        try {

            PdfWriter.getInstance(document, new FileOutputStream(new File(fName)));

            //open
            document.open();

            PdfPTable table = new PdfPTable(5);
            PdfPCell cell = new PdfPCell(new Phrase("Receipt #" + dateStamp));
            cell.setColspan(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);

            //#1
            cell = new PdfPCell(new Phrase("No"));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            //#2
            cell = new PdfPCell(new Phrase("Product name"));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            //#3
            cell = new PdfPCell(new Phrase("EAN"));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            //#4
            cell = new PdfPCell(new Phrase("Price"));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            //#5
            cell = new PdfPCell(new Phrase("Quantity"));
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            for (int i = 0; i < productsInCart.size(); i++) {
                cell = new PdfPCell(new Phrase(String.valueOf(i + 1)));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(productsInCart.get(i).getProduct().getName()));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(productsInCart.get(i).getProduct().getEan()));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(productsInCart.get(i).getProduct().getPrice().toString()));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(productsInCart.get(i).getQuantity())));
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }


            document.add(table);

            Font f = new Font();
            f.setStyle(Font.BOLD);

            Paragraph p = new Paragraph("Total price: " + totalPrice, f);
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);

            //close
            document.close();

            System.out.println("Done");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return receipt;
    }


}
