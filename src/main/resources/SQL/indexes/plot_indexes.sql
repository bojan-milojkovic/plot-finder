CREATE BITMAP INDEX MyBitmapIndex ON plot(`power`,`water`,`gas`,`sewer`,`internet`,`garage`,`house`,`farming`,`grazing`,`orchard`);

CREATE INDEX CountryIndex ON plot(`country`);

CREATE INDEX CityIndex ON plot(`city`);

CREATE INDEX DistrictIndex ON plot(`district`);

CREATE INDEX PriceIndex ON plot(`price`);

CREATE INDEX SizeIndex ON plot(`size`);



CREATE INDEX ll_x_index ON plot(`ll_x`);
CREATE INDEX ll_y_index ON plot(`ll_y`);
CREATE INDEX ur_x_index ON plot(`ur_x`);
CREATE INDEX ur_y_index ON plot(`ur_y`);