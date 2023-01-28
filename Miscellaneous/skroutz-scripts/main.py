import json
import os
import re
import sqlite3


def load_json_files(directory):
    directories = os.listdir(directory)
    return [load_json(os.path.join(directory, file)) for file in directories]


def load_json(filename):
    file = open(filename)
    json_contents = json.load(file)
    file.close()
    return json_contents


class Product:
    name = None
    price = None
    units = None
    weight_in_grams = None
    product_url = None

    def __init__(self, name):
        self.name = name

    def __str__(self) -> str:
        return "Product: \n name: {},\n price: {},\n units: {},\n weight_in_grams: {}\n".format(
            self.name,
            self.price,
            self.units,
            self.weight_in_grams
        )

    def __repr__(self) -> str:
        return self.__str__()

    def set_price(self, price):
        self.price = price
        return self

    def set_units(self, units):
        self.units = units
        return self

    def set_weight(self, weight):
        self.weight_in_grams = weight
        return self

    def set_product_url(self, product_url):
        self.product_url = product_url
        return self

    def data_is_valid(self):
        if self.name is None:
            return False
        if self.price is None or self.price <= 0.0:
            return False
        if self.weight_in_grams is None or self.weight_in_grams <= 0.0:
            return False
        if self.units is None or self.units <= 0.0:
            return False
        if self.product_url is None:
            return False
        return True

    def price_per_unit(self):
        return self.price / self.units

    def price_per_unit_and_weight(self):
        return self.price / (self.units * self.weight_in_grams)


def parse_price(raw_value):
    try:
        return float(
            str(raw_value)
            .replace("€", "")
            .replace('â\x82¬', "")
            .replace(",", ".")
            .strip()
        )
    except Exception as ex:
        print("ERROR parsing value: {}. Exception".format(raw_value, str(ex)))
        return None


def parse_weight(product_name):
    match = re.search(r"([0-9]+)gr", product_name)
    if len(match.groups()) == 1:
        try:
            return float(match.group(1))
        except Exception as ex:
            print("Exception: {}".format(str(ex)))
    return None


def parse_units(product_name):
    match = re.search(r"([0-9]+)τμχ", product_name)
    if match is not None and len(match.groups()) == 1:
        try:
            return float(match.group(1))
        except Exception as ex:
            print("Exception: {}".format(str(ex)))
            return None
    return 1


def prepare_db():
    db_connection = sqlite3.connect("products.db")

    cursor = db_connection.cursor()

    cursor.execute("DROP TABLE IF EXISTS products")
    cursor.execute("""
            CREATE TABLE "products" (
            "id" INTEGER PRIMARY KEY AUTOINCREMENT,
            "name" TEXT NOT NULL,
            "units"	INTEGER NOT NULL,
            "weight" REAL NOT NULL,
            "price" REAL NOT NULL,
            "price_per_unit" REAL NOT NULL,
            "price_per_unit_and_weight" REAL NOT NULL,
            "product_url" INTEGER NOT NULL
        );
    """)

    return db_connection


def save_products(db_connection, products):
    # Larger example that inserts many records at a time
    purchases = [(product.name, product.units, product.weight_in_grams, product.price, product.price_per_unit(),
                  product.price_per_unit_and_weight(),
                  product.product_url) for product
                 in products]
    db_connection.executemany(
        'INSERT INTO products(name,units,weight,price,price_per_unit,price_per_unit_and_weight,product_url) VALUES (?,?,?,?,?,?,?)',
        purchases)
    db_connection.commit()


if __name__ == '__main__':

    products = []
    page_items = load_json_files("./input-files")

    print("Load data from input directory")
    for item in page_items:
        for sku in item['skus']:
            name = sku['name']
            product = Product(name) \
                .set_price(parse_price(sku['price'])) \
                .set_weight(parse_weight(sku['name'])) \
                .set_units(parse_units(name)) \
                .set_product_url("https://skroutz.gr/{}".format(sku['sku_url']))

            if product.data_is_valid() is False:
                print("Product is not valid!")
                print(str(product))
            else:
                products.append(product)
    print("Parsed {} products".format(len(products)))
    print("Finished")

    print("Saving data to database")
    db_connection = prepare_db()
    products = sorted(products, key=lambda x: x.price_per_unit_and_weight())
    save_products(db_connection, products)
    db_connection.close()
    print("Finished")
