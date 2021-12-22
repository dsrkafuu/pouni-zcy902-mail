const path = require('path');
const glob = require('glob');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

const jsInputs = glob.sync('./src/js/*.js');
jsInputs.push('./src/css/styles.js');
const jsEntrys = {};
jsInputs.forEach((entry) => {
  const basename = path.basename(entry, '.js');
  jsEntrys[basename] = { import: entry, filename: `assets/${basename}.js` };
});

const htmlInputs = glob.sync('./src/*.ejs');
const htmlPlugins = [];
htmlInputs.forEach((input) => {
  const basename = path.basename(input, '.ejs');
  htmlPlugins.push(
    new HtmlWebpackPlugin({
      template: input,
      filename: `${basename}.html`,
      inject: false,
    })
  );
});

module.exports = {
  mode: 'development',
  entry: { ...jsEntrys },
  devtool: 'source-map',
  output: {
    filename: '[name].js',
    path: path.resolve(__dirname, '../main/resources/static'),
  },
  externals: {
    jquery: 'jQuery',
  },
  module: {
    rules: [
      {
        test: /\.s?css$/i,
        use: [
          MiniCssExtractPlugin.loader,
          { loader: 'css-loader', options: { url: false } },
        ],
      },
      {
        test: /\.ejs$/,
        loader: 'ejs-compiled-loader',
      },
    ],
  },
  plugins: [
    new CleanWebpackPlugin(),
    new MiniCssExtractPlugin({ filename: 'assets/main.css' }),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: './public',
          to: path.resolve(__dirname, '../main/resources/static'),
        },
      ],
    }),
    ...htmlPlugins,
  ],
  devServer: {
    port: 3000,
    hot: false,
    liveReload: false,
    watchFiles: ['src/**/*'],
    proxy: {
      '**': 'http://localhost:8085',
    },
  },
};
