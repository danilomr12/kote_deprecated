/**
 * Created by danilo on 01/04/14.
 */

/* Modified Thompson Tau - calculated by tau = t*(n-1)/sqrt(n)*sqrt(n-2+t²)
 n is the number of data points
 t is the student’s t value, based on α = 0.05 and df = n-2 (note that here df = n-2 instead of n-1). In Excel,
 we calculate t as TINV(α, df), i.e., here t = TINV(α, n-2)
 */
var tau = [1.15114098198125,
    1.425,
    1.57122137072398,
    1.65626607396521,
    1.71102755872556,
    1.7490784053906,
    1.77702294756349,
    1.79841000495057,
    1.81530566613323,
    1.82899162749097,
    1.84030441033769,
    1.84981299605965,
    1.85791782105934,
    1.86490888297272,
    1.87100136908321,
    1.87635832955106,
    1.88110557504102,
    1.88534174100679,
    1.889145254242,
    1.89257925992902,
    1.89569517351593,
    1.89853528543143,
    1.90113470086547,
    1.90352280464061,
    1.90572438155644,
    1.9077604832138,
    1.90964910584322,
    1.91140572554655,
    1.91304372477414,
    1.91457473498837,
    1.91600891412878,
    1.91735517291223,
    1.91862136065163,
    1.91981441880018,
    1.92094050857962,
    1.92200511765778,
    1.92301314978254,
    1.92396900046758,
    1.92487662119912,
    1.9257395741453,
    1.9265610789683,
    1.92734405303837,
    1.92809114611004,
    1.92880477033067,
    1.92948712629835,
    1.93014022576318,
    1.93076591146579,
    1.93136587452587,
    1.93194166972653,
    1.93249472898578,
    1.93302637326131,
    1.93353782309694,
    1.93403020798844,
    1.93450457471995,
    1.93496189480074,
    1.93540307111359,
    1.93582894387062,
    1.93624029595931,
    1.93663785775037,
    1.93702231142955,
    1.93739429490765,
    1.93775440535571,
    1.93810320240681,
    1.93844121106049,
    1.93876892432159,
    1.93908680560136,
    1.93939529090554,
    1.93969479083107,
    1.93998569239074,
    1.94026836068273,
    1.94054314042042,
    1.94081035733565,
    1.94107031946772,
    1.94132331834868,
    1.94156963009462,
    1.94180951641149,
    1.94204322552318,
    1.94227099302875,
    1.94249304269512,
    1.94270958719069,
    1.94292082876513,
    1.94312695987973,
    1.94332816379256,
    1.94352461510216,
    1.94371648025307,
    1.94390391800643,
    1.94408707987831,
    1.94426611054836,
    1.94444114824114,
    1.9446123250822,
    1.9447797674308,
    1.94494359619113,
    1.94510392710358,
    1.94526087101753,
    1.94541453414701,
    1.94556501831055,
    1.9457124211562,
    1.94585683637293];
function computeStdDeviation(values) {
    var mean = computeMean(values);
    var total = 0;
    for (var i = 0; i < values.length; i++) {
        var d = values[i] - mean;
        total += (d * d);
    }
    return Math.sqrt(total / (values.length - 1));
}
function computeMean(values) {
    var total = 0;
    for (var i = 0; i < values.length; i++)
        total += values[i];
    return total / values.length
}

/*var computeSquare = function (v) { return v * v; };
 var computeSum = function (a, b) { return a + b; };
 var computeMeanF = function (values) { return values.reduce(computeSum) / values.length; };

 var computeStdDeviationF = function (values) {
 var mean = computeMeanF(values);
 var deviations = values.map(function (v) { return v - mean; });
 return Math.sqrt(deviations.map(computeSquare).reduce(computeSum) / (values.length - 1));
 };

 */

/**
 * Map function that emit id of products and the prices of it removing the outliers from respostas
 */
var mapFunctionRemoveOutliersOnMapWithProductIdAsKey = function() {
    var key = this.idProduto;
    var precos = [];
    var respostas = this.respostas;
    var valuesWithoutOutliers = [];
    var outliers = [];
    if(respostas){
        for (var i=0; i<respostas.length; i++){
            precos.push(respostas[i].preco)
        }
        if(precos.length>=3){
            var tauN = tau[precos.length-3];
            var stdev = computeStdDeviation(precos);
            var mean = computeMean(precos);
            var tauStdev = tauN * stdev;
            for (var idx = 0; idx < precos.length; idx++) {
                var preco = precos[idx];
                var delta = Math.abs(preco-mean);
                if(delta> tauStdev){
                    //outlier
                    outliers.push(preco);
                    print ("\nOutlier, idProduto: "+key+" preco"+preco + "outros precos"+ tojson(precos));
                }else{
                    valuesWithoutOutliers.push(preco);
                }
            }
        }else{

            valuesWithoutOutliers = precos;
        }
        if(valuesWithoutOutliers !=null && valuesWithoutOutliers.length>0){

            //emit(this.descricao.replace(/\s{2,}/g," ").replace(/\xA0/g," "), {id: this.idProduto,embalagem: this.embalagem, precos: valuesWithoutOutliers, outliers: outliers});
            emit(key, {descricao: this.descricao, embalagem: this.embalagem, precos: valuesWithoutOutliers, precosOutliers: outliers});
        }else{
            emit("semrespostas", {descricao: this.descricao, embalagem: this.embalagem, precos: valuesWithoutOutliers, precosOutliers: outliers});
        }
    }
};

/**
 * @param keyCustId - Emited key by the map function (id do produto)
 * @param values - List with all prices of the product
 * @returns Average value of product prices
 */
var reduceFunctionRemoveOutliersOnMapWithProductIdAsKey = function(keyCustId, values) {
    /*  var reducedVal = {
     ids: [],
     embalagem: values[0].embalagem,
     precosSemOutliers: [],
     outliers:[],
     media: 0
     };
     */
    var reducedVal = {
        descricao: values[0].descricao,
        embalagem: values[0].embalagem,
        precosSemOutliers: [],
        precosDeOutliers:[],
        media: 0,
        totalProdutos: 0
    };
    var total = 0;
    var precosLength = values.length;
    var totalItens = 0;
    reducedVal.totalProdutos ++;
    if(values != null && values.length>0){
        for (var idx = 0; idx < precosLength; idx++) {
            var value = values[idx];

            /*        reducedVal.ids.push(value.id);*/

            if(value){
                if(value.precos != null){
                    var precos2 = value.precos;
                    for(var i=0;i<precos2.length; i++){
                        total += precos2[i];
                        reducedVal.precosSemOutliers.push(precos2[i]);
                        totalItens++;
                    }
                }
                if(value.precosOutliers!= null){
                    var outliers = value.precosOutliers;

                    for(var y=0;y<outliers.length; y++){
                        reducedVal.precosDeOutliers.push(outliers[y]);

                    }
                }
            }else{
                print ("\nSingle value: "+tojson(value)+"from emited key:"+tojson(keyCustId) +"has no properties");
            }
        }
        reducedVal.media = (total/totalItens);
    }else{
        print ("\nAll values have no properties, values emited are: key:"+tojson(keyCustId)+ "values " +tojson(values));
    }
    return reducedVal;
};

db.item.mapReduce(
    mapFunctionRemoveOutliersOnReduceWithProductIdAsKey,
    reduceFunctionRemoveOutliersOnReduceWithProductIdAsKey,
    {
        out: "map_reduce_example_outliers_removed_on_reduce" ,
        scope: {
            tau: tau,
            computeMean: computeMean,
            computeStdDeviation: computeStdDeviation
        }
    }
);


var mapFunctionRemoveOutliersOnReduceWithProductIdAsKey = function() {
    var key = this.idProduto;
    var precos = [];
    var respostas = this.respostas;
    var valuesWithoutOutliers = [];
    var outliers = [];
    if(respostas){
        for (var i=0; i<respostas.length; i++){
            precos.push(respostas[i].preco)
        }

                //emit(this.descricao.replace(/\s{2,}/g," ").replace(/\xA0/g," "), {id: this.idProduto,embalagem: this.embalagem, precos: valuesWithoutOutliers, outliers: outliers});
        emit(key, {descricao: this.descricao, embalagem: this.embalagem, precos: precos});

    }else{
        emit("semrespostas", {descricao: this.descricao, embalagem: this.embalagem, precos: precos});
    }
};

var reduceFunctionRemoveOutliersOnReduceWithProductIdAsKey = function(keyCustId, values) {
    /*  var reducedVal = {
     ids: [],
     embalagem: values[0].embalagem,
     precosSemOutliers: [],
     outliers:[],
     media: 0
     };
     */
    var reducedVal = {
        descricao: values[0].descricao,
        embalagem: values[0].embalagem,
        precosSemOutliers: [],
        precosDeOutliers:[],
        media: 0,
        totalProdutos: 0
    };
    var total = 0;
    var precosLength = values.length;
    var totalItens = 0;
    var tempPrecos = [];

    if(values != null && values.length>0){
        for (var idx = 0; idx < precosLength; idx++) {
            var value = values[idx];
            if(value){
                if(value.precos != null){
                    var precos2 = value.precos;
                    reducedVal.totalProdutos ++;
                    for(var i=0;i<precos2.length; i++){
                        tempPrecos.push(precos2[i]);
                    }
                }
            }else{
                print ("\nSingle value: "+tojson(value)+"from emited key: "+tojson(keyCustId) +"has no properties");
            }
        }

        if(tempPrecos.length>=3){
            var tauN = tau[tempPrecos.length-3];
            var stdev = computeStdDeviation(tempPrecos);
            var mean = computeMean(tempPrecos);
            var tauStdev = tauN * stdev;

            for (var idx = 0; idx < tempPrecos.length; idx++) {
                var preco = tempPrecos[idx];
                var delta = Math.abs(preco-mean);
                if(delta> tauStdev){
                    //outlier
                    reducedVal.precosDeOutliers.push(preco);
                    print ("\nOutlier, idProduto: "+keyCustId+" preco: " + preco + "outros precos: "+ tojson(tempPrecos));
                }else{
                    reducedVal.precosSemOutliers.push(preco);
                    total += preco;
                    totalItens++;
                }
            }
        }else{
            valuesWithoutOutliers = tempPrecos;
        }

        reducedVal.media = (total/totalItens);
    }else{
        print ("\nAll values have no properties, values emited are: key: "+tojson(keyCustId)+ "values: " +tojson(values));
    }
    return reducedVal;
};





db.map_reduce_example_by_id.find({"value.descricao": /HA-LA/}).pretty();
db.map_reduce_example.find({"_id": /HA-LA/}).pretty();