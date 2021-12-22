const path = require('path');
const glob = require('glob');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const mode =
  process.env.NODE_ENV === 'development' ? 'development' : 'production';
const jsEntrys = glob.sync('./src/js/*.js');

module.exports = {
  mode,
  entry: jsEntrys,
  output: {
    filename: '[name].js',
    path: path.resolve(__dirname, '../main/resources/static'),
  },
  plugins: [],
  devServer: {
    port: 3000,
  },
};
