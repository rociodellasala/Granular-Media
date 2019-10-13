D = [0.15, 0.18, 0.22, 0.25];

caudal = [120, 180, 260, 370];
error = [0.7, 2.0, 2.8, 1.4];

scatter(D,caudal, "markersize", 15, "filled");
j = errorbar(D,caudal,error,".");
set (j, "color", "b", "markersize", 50, "linewidth", 3);
hold on;

axis([0.13 0.27 90 390]);
xbounds = xlim();
set(gca, 'xtick', xbounds(1):0.01:xbounds(2));
ybounds = ylim();
set(gca, 'ytick', ybounds(1):30:ybounds(2));

xlabel("D [m]", "fontsize", 50);
ylabel("Caudal [particulas/s]", "fontsize", 50);
set(gca, "linewidth", 1, "fontsize", 50);
