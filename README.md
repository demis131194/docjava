[Проект DocJava](https://javaops.ru/view/docjava)
===============================

### Работа с документами XML (JAXB, StAX, XPath, XSLT, XSD), PDF(Apache FOP, iText), Excel(Apache POI)

-------------------------------------------------------------
- Stack: [JDK 17](http://jdk.java.net/17/), Spring Shell, JAXB, StAX, XPath, XSLT, XSD, XSL-FO, [Apache FOP](https://xmlgraphics.apache.org/fop/), [Apache POI](https://poi.apache.org/), [iText](https://itextpdf.com)

Функционал: XML файл с пользователями и едой (из [приложения курса TopJava](http://javaops-demo.ru/topjava)) валидируем и обрабатываем с преобразованием в другой формат

```
Commands:
    xsd: Validate XML files against XSD (XML Schema)
    jaxb: Process XML file via JAXB
    stax: Process XML file via StAX
    xpath: Evaluate XPath against XML
    xslt: Transform XML with XSLT
    pdf-fop: Convert XML to PDF via Apache FOP
    iText: Convert XML to PDF via iText
    excel-poi: Convert XML to Excel via Apache POI
          
Parameters:
  -i (input) Input file
  -o (output) Output file
  -s (schema) XSD schema file (for xsd and jaxb)
  -e (exp) XPath expression (for xpath) 
  -x (xsl) XSL transformation file (for xslt) 
  -e (email) user email (for stax) 
  -f (filter) Filter params: startDate=2023-01-30,endDate=2023-01-30,srtartTime=11:00,endTime=19:00
  -t (template) Transform template file (for pdf/excel transformation)
```

## Создание <a href="https://reflectoring.io/spring-shell/">консольного Spring Shell приложения</a>
> commit: Init Spring Shell App

```
mvn spring-boot:run  (или как обычное Spring Boot приложение из IDEA)
```

После старта приложения отображается консоль для ввода команд:

```
shell:>help
AVAILABLE COMMANDS ...

shell:>help help - info about help
```

## Создаем `usersWithMeals.xml` данные пользователей с едой и схему `usersWithMeals.xsd` 
> commit: 1_xml_xsd

- Если с XML не имели дело, ознакомтесь с основами: 
  - [Что такое XML](https://habr.com/ru/articles/524288/)
- Дополнительно:
  - [Attribute vs Element](http://stackoverflow.com/questions/33746/xml-attribute-vs-xml-element#33757)
  - [Лекции по XML](http://genberm.narod.ru/xml/lections.html)

Схему можно сгенерировать в IDEA (в контекстном меню в xml файле -> _Generate XSD Schema from XML File..._) и поправить вручную
