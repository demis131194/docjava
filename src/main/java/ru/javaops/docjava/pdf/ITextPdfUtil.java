package ru.javaops.docjava.pdf;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import jakarta.xml.bind.JAXBException;
import ru.javaops.docjava.schema.Meals;
import ru.javaops.docjava.schema.RoleTypes;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.schema.UsersWithMeals;
import ru.javaops.docjava.xml.jaxb.JaxbUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javaops.docjava.pdf.ITextPdfConverter.DARK_GREEN;
import static ru.javaops.docjava.pdf.ITextPdfConverter.createCell;
import static ru.javaops.docjava.util.Util.format;

public class ITextPdfUtil {

    public static void convert(File inputFile, File outputFile, Map<String, Object> params) throws JAXBException, IOException {
        UsersWithMeals usersWithMeals = JaxbUtil.unmarshalAndFilter(inputFile, params);
        ITextPdfConverter.convert(outputFile,
                document -> {
                    PdfFont font = ITextPdfConverter.createRegisteredFont("Verdana");
                    document.setFont(font);
                    document.add(new Paragraph().add(new Text("Users with meals").setFontSize(18)));
                    document.add(new LineSeparator(new SolidLine()).setMarginTop(4));
                    usersWithMeals.getUsers().getUser().forEach(user -> {
                        document.add(addUserName(user));
                        document.add(addUserInfo(user));
                        document.add(addTableMeals(user.getMeals()));
                    });

                });
        System.out.println("Convert to PDF completed successfully, result in " + outputFile.getAbsolutePath());
    }

    private static Paragraph addUserName(User user) {
        Paragraph userName = new Paragraph().setMarginTop(20);
        userName.add(new Text(user.getName() + ": ").setBold());
        userName.add(user.isEnabled() ?
                new Text("enabled") {{
                    setFontColor(DARK_GREEN);
                }} :
                new Text("disabled") {{
                    setFontColor(DeviceRgb.RED);
                }});
        return userName;
    }

    private static Paragraph addUserInfo(User user) {
        Paragraph userInfo = new Paragraph();
//        https://stackoverflow.com/a/41430483/548473
        userInfo.add("Email: " + user.getEmail() + "\n")
                .add("CaloriesPerDay: " + user.getCaloriesPerDay() + "\n")
                .add("Registered: " + format(user.getRegistered()) + "\n")
                .add("Roles: ")
                .add(user.getRoles().isEmpty() ?
                        "none" :
                        user.getRoles().stream()
                                .map(RoleTypes::getValue)
                                .collect(Collectors.joining(", "))
                );
        return userInfo;
    }

    private static Table addTableMeals(Meals meals) {
        Table table = new Table(3);
        Stream.of("Date", "Description", "Calories").forEach(rowHeader -> {
            table.addCell(createCell(rowHeader, null).setBold());
        });
        if (meals != null) {
            meals.getMeal().forEach(meal -> {
                Color color = meal.getExcess() ? DeviceRgb.RED : DARK_GREEN;
                table.addCell(createCell(format(meal.getDateTime()), color));
                table.addCell(createCell(meal.getValue(), color));
                table.addCell(createCell(String.valueOf(meal.getCalories()), color));
            });
        }
        return table;
    }
}
