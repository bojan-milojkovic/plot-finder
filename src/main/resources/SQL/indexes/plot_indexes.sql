CREATE INDEX ll_x_index ON plotfinder.plot(ll_x);
CREATE INDEX ll_y_index ON plotfinder.plot(ll_y);
CREATE INDEX ur_x_index ON plotfinder.plot(ur_x);
CREATE INDEX ur_y_index ON plotfinder.plot(ur_y);

CREATE INDEX PriceIndex ON plotfinder.plot(price);
CREATE INDEX SizeIndex ON plotfinder.plot(size);

CREATE INDEX CountryIndex ON plotfinder.plot(country);
CREATE INDEX CityIndex ON plotfinder.plot(city);
CREATE INDEX DistrictIndex ON plotfinder.plot(district);

CREATE INDEX FlagsIndex ON plotfinder.flags(plot_id,flag);